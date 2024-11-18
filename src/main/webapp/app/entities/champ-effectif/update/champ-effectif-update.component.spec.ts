import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEffectif } from 'app/entities/effectif/effectif.model';
import { EffectifService } from 'app/entities/effectif/service/effectif.service';
import { ChampEffectifService } from '../service/champ-effectif.service';
import { IChampEffectif } from '../champ-effectif.model';
import { ChampEffectifFormService } from './champ-effectif-form.service';

import { ChampEffectifUpdateComponent } from './champ-effectif-update.component';

describe('ChampEffectif Management Update Component', () => {
  let comp: ChampEffectifUpdateComponent;
  let fixture: ComponentFixture<ChampEffectifUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let champEffectifFormService: ChampEffectifFormService;
  let champEffectifService: ChampEffectifService;
  let effectifService: EffectifService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ChampEffectifUpdateComponent],
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
      .overrideTemplate(ChampEffectifUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChampEffectifUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    champEffectifFormService = TestBed.inject(ChampEffectifFormService);
    champEffectifService = TestBed.inject(ChampEffectifService);
    effectifService = TestBed.inject(EffectifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Effectif query and add missing value', () => {
      const champEffectif: IChampEffectif = { id: 456 };
      const effectif: IEffectif = { id: 4748 };
      champEffectif.effectif = effectif;

      const effectifCollection: IEffectif[] = [{ id: 19948 }];
      jest.spyOn(effectifService, 'query').mockReturnValue(of(new HttpResponse({ body: effectifCollection })));
      const additionalEffectifs = [effectif];
      const expectedCollection: IEffectif[] = [...additionalEffectifs, ...effectifCollection];
      jest.spyOn(effectifService, 'addEffectifToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ champEffectif });
      comp.ngOnInit();

      expect(effectifService.query).toHaveBeenCalled();
      expect(effectifService.addEffectifToCollectionIfMissing).toHaveBeenCalledWith(
        effectifCollection,
        ...additionalEffectifs.map(expect.objectContaining),
      );
      expect(comp.effectifsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const champEffectif: IChampEffectif = { id: 456 };
      const effectif: IEffectif = { id: 13570 };
      champEffectif.effectif = effectif;

      activatedRoute.data = of({ champEffectif });
      comp.ngOnInit();

      expect(comp.effectifsSharedCollection).toContain(effectif);
      expect(comp.champEffectif).toEqual(champEffectif);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChampEffectif>>();
      const champEffectif = { id: 123 };
      jest.spyOn(champEffectifFormService, 'getChampEffectif').mockReturnValue(champEffectif);
      jest.spyOn(champEffectifService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ champEffectif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: champEffectif }));
      saveSubject.complete();

      // THEN
      expect(champEffectifFormService.getChampEffectif).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(champEffectifService.update).toHaveBeenCalledWith(expect.objectContaining(champEffectif));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChampEffectif>>();
      const champEffectif = { id: 123 };
      jest.spyOn(champEffectifFormService, 'getChampEffectif').mockReturnValue({ id: null });
      jest.spyOn(champEffectifService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ champEffectif: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: champEffectif }));
      saveSubject.complete();

      // THEN
      expect(champEffectifFormService.getChampEffectif).toHaveBeenCalled();
      expect(champEffectifService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChampEffectif>>();
      const champEffectif = { id: 123 };
      jest.spyOn(champEffectifService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ champEffectif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(champEffectifService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEffectif', () => {
      it('Should forward to effectifService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(effectifService, 'compareEffectif');
        comp.compareEffectif(entity, entity2);
        expect(effectifService.compareEffectif).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
