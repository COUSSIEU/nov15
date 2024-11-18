import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ChampEffectifDetailComponent } from './champ-effectif-detail.component';

describe('ChampEffectif Management Detail Component', () => {
  let comp: ChampEffectifDetailComponent;
  let fixture: ComponentFixture<ChampEffectifDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChampEffectifDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./champ-effectif-detail.component').then(m => m.ChampEffectifDetailComponent),
              resolve: { champEffectif: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ChampEffectifDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChampEffectifDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load champEffectif on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ChampEffectifDetailComponent);

      // THEN
      expect(instance.champEffectif()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
