import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ChampEffectif e2e test', () => {
  const champEffectifPageUrl = '/champ-effectif';
  const champEffectifPageUrlPattern = new RegExp('/champ-effectif(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const champEffectifSample = {};

  let champEffectif;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/champ-effectifs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/champ-effectifs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/champ-effectifs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (champEffectif) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/champ-effectifs/${champEffectif.id}`,
      }).then(() => {
        champEffectif = undefined;
      });
    }
  });

  it('ChampEffectifs menu should load ChampEffectifs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('champ-effectif');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChampEffectif').should('exist');
    cy.url().should('match', champEffectifPageUrlPattern);
  });

  describe('ChampEffectif page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(champEffectifPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChampEffectif page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/champ-effectif/new$'));
        cy.getEntityCreateUpdateHeading('ChampEffectif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', champEffectifPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/champ-effectifs',
          body: champEffectifSample,
        }).then(({ body }) => {
          champEffectif = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/champ-effectifs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [champEffectif],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(champEffectifPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChampEffectif page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('champEffectif');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', champEffectifPageUrlPattern);
      });

      it('edit button click should load edit ChampEffectif page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChampEffectif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', champEffectifPageUrlPattern);
      });

      it('edit button click should load edit ChampEffectif page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChampEffectif');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', champEffectifPageUrlPattern);
      });

      it('last delete button click should delete instance of ChampEffectif', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('champEffectif').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', champEffectifPageUrlPattern);

        champEffectif = undefined;
      });
    });
  });

  describe('new ChampEffectif page', () => {
    beforeEach(() => {
      cy.visit(`${champEffectifPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ChampEffectif');
    });

    it('should create an instance of ChampEffectif', () => {
      cy.get(`[data-cy="nom"]`).type('chef de cuisine');
      cy.get(`[data-cy="nom"]`).should('have.value', 'chef de cuisine');

      cy.get(`[data-cy="valeur"]`).type('5979');
      cy.get(`[data-cy="valeur"]`).should('have.value', '5979');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        champEffectif = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', champEffectifPageUrlPattern);
    });
  });
});
