import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { EffectifService } from '../service/effectif.service';
import { IEffectif } from '../effectif.model';
import { EffectifFormService } from './effectif-form.service';

import { EffectifUpdateComponent } from './effectif-update.component';

describe('Effectif Management Update Component', () => {
  let comp: EffectifUpdateComponent;
  let fixture: ComponentFixture<EffectifUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let effectifFormService: EffectifFormService;
  let effectifService: EffectifService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EffectifUpdateComponent],
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
      .overrideTemplate(EffectifUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EffectifUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    effectifFormService = TestBed.inject(EffectifFormService);
    effectifService = TestBed.inject(EffectifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const effectif: IEffectif = { id: 456 };

      activatedRoute.data = of({ effectif });
      comp.ngOnInit();

      expect(comp.effectif).toEqual(effectif);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEffectif>>();
      const effectif = { id: 123 };
      jest.spyOn(effectifFormService, 'getEffectif').mockReturnValue(effectif);
      jest.spyOn(effectifService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ effectif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: effectif }));
      saveSubject.complete();

      // THEN
      expect(effectifFormService.getEffectif).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(effectifService.update).toHaveBeenCalledWith(expect.objectContaining(effectif));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEffectif>>();
      const effectif = { id: 123 };
      jest.spyOn(effectifFormService, 'getEffectif').mockReturnValue({ id: null });
      jest.spyOn(effectifService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ effectif: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: effectif }));
      saveSubject.complete();

      // THEN
      expect(effectifFormService.getEffectif).toHaveBeenCalled();
      expect(effectifService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEffectif>>();
      const effectif = { id: 123 };
      jest.spyOn(effectifService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ effectif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(effectifService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
