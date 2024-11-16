import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PopulationService } from '../service/population.service';
import { IPopulation } from '../population.model';
import { PopulationFormService } from './population-form.service';

import { PopulationUpdateComponent } from './population-update.component';

describe('Population Management Update Component', () => {
  let comp: PopulationUpdateComponent;
  let fixture: ComponentFixture<PopulationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let populationFormService: PopulationFormService;
  let populationService: PopulationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PopulationUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PopulationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PopulationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    populationFormService = TestBed.inject(PopulationFormService);
    populationService = TestBed.inject(PopulationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const population: IPopulation = { id: 456 };

      activatedRoute.data = of({ population });
      comp.ngOnInit();

      expect(comp.population).toEqual(population);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPopulation>>();
      const population = { id: 123 };
      jest.spyOn(populationFormService, 'getPopulation').mockReturnValue(population);
      jest.spyOn(populationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ population });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: population }));
      saveSubject.complete();

      // THEN
      expect(populationFormService.getPopulation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(populationService.update).toHaveBeenCalledWith(expect.objectContaining(population));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPopulation>>();
      const population = { id: 123 };
      jest.spyOn(populationFormService, 'getPopulation').mockReturnValue({ id: null });
      jest.spyOn(populationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ population: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: population }));
      saveSubject.complete();

      // THEN
      expect(populationFormService.getPopulation).toHaveBeenCalled();
      expect(populationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPopulation>>();
      const population = { id: 123 };
      jest.spyOn(populationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ population });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(populationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
