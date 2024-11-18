import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MaterielService } from '../service/materiel.service';
import { IMateriel } from '../materiel.model';
import { MaterielFormService } from './materiel-form.service';

import { MaterielUpdateComponent } from './materiel-update.component';

describe('Materiel Management Update Component', () => {
  let comp: MaterielUpdateComponent;
  let fixture: ComponentFixture<MaterielUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materielFormService: MaterielFormService;
  let materielService: MaterielService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaterielUpdateComponent],
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
      .overrideTemplate(MaterielUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterielUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materielFormService = TestBed.inject(MaterielFormService);
    materielService = TestBed.inject(MaterielService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const materiel: IMateriel = { id: 456 };

      activatedRoute.data = of({ materiel });
      comp.ngOnInit();

      expect(comp.materiel).toEqual(materiel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateriel>>();
      const materiel = { id: 123 };
      jest.spyOn(materielFormService, 'getMateriel').mockReturnValue(materiel);
      jest.spyOn(materielService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materiel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materiel }));
      saveSubject.complete();

      // THEN
      expect(materielFormService.getMateriel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materielService.update).toHaveBeenCalledWith(expect.objectContaining(materiel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateriel>>();
      const materiel = { id: 123 };
      jest.spyOn(materielFormService, 'getMateriel').mockReturnValue({ id: null });
      jest.spyOn(materielService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materiel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materiel }));
      saveSubject.complete();

      // THEN
      expect(materielFormService.getMateriel).toHaveBeenCalled();
      expect(materielService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateriel>>();
      const materiel = { id: 123 };
      jest.spyOn(materielService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materiel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materielService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
