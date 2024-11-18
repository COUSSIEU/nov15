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

describe('Materiel e2e test', () => {
  const materielPageUrl = '/materiel';
  const materielPageUrlPattern = new RegExp('/materiel(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const materielSample = {};

  let materiel;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/materiels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/materiels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/materiels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (materiel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/materiels/${materiel.id}`,
      }).then(() => {
        materiel = undefined;
      });
    }
  });

  it('Materiels menu should load Materiels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('materiel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Materiel').should('exist');
    cy.url().should('match', materielPageUrlPattern);
  });

  describe('Materiel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(materielPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Materiel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/materiel/new$'));
        cy.getEntityCreateUpdateHeading('Materiel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', materielPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/materiels',
          body: materielSample,
        }).then(({ body }) => {
          materiel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/materiels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/materiels?page=0&size=20>; rel="last",<http://localhost/api/materiels?page=0&size=20>; rel="first"',
              },
              body: [materiel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(materielPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Materiel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('materiel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', materielPageUrlPattern);
      });

      it('edit button click should load edit Materiel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Materiel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', materielPageUrlPattern);
      });

      it('edit button click should load edit Materiel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Materiel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', materielPageUrlPattern);
      });

      it('last delete button click should delete instance of Materiel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('materiel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', materielPageUrlPattern);

        materiel = undefined;
      });
    });
  });

  describe('new Materiel page', () => {
    beforeEach(() => {
      cy.visit(`${materielPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Materiel');
    });

    it('should create an instance of Materiel', () => {
      cy.get(`[data-cy="etat"]`).type('que ferme');
      cy.get(`[data-cy="etat"]`).should('have.value', 'que ferme');

      cy.get(`[data-cy="release"]`).type('en dépit de fourbe foule');
      cy.get(`[data-cy="release"]`).should('have.value', 'en dépit de fourbe foule');

      cy.get(`[data-cy="modele"]`).type('raide');
      cy.get(`[data-cy="modele"]`).should('have.value', 'raide');

      cy.get(`[data-cy="sorte"]`).type('pschitt');
      cy.get(`[data-cy="sorte"]`).should('have.value', 'pschitt');

      cy.get(`[data-cy="site"]`).type('au prix de amorphe');
      cy.get(`[data-cy="site"]`).should('have.value', 'au prix de amorphe');

      cy.get(`[data-cy="region"]`).type("aujourd'hui");
      cy.get(`[data-cy="region"]`).should('have.value', "aujourd'hui");

      cy.get(`[data-cy="mission"]`).type('tic-tac de par délégation');
      cy.get(`[data-cy="mission"]`).should('have.value', 'tic-tac de par délégation');

      cy.get(`[data-cy="entite"]`).type('hebdomadaire parce que');
      cy.get(`[data-cy="entite"]`).should('have.value', 'hebdomadaire parce que');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        materiel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', materielPageUrlPattern);
    });
  });
});
