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

describe('Effectif e2e test', () => {
  const effectifPageUrl = '/effectif';
  const effectifPageUrlPattern = new RegExp('/effectif(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const effectifSample = {};

  let effectif;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/effectifs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/effectifs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/effectifs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (effectif) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/effectifs/${effectif.id}`,
      }).then(() => {
        effectif = undefined;
      });
    }
  });

  it('Effectifs menu should load Effectifs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('effectif');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Effectif').should('exist');
    cy.url().should('match', effectifPageUrlPattern);
  });

  describe('Effectif page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(effectifPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Effectif page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/effectif/new$'));
        cy.getEntityCreateUpdateHeading('Effectif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', effectifPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/effectifs',
          body: effectifSample,
        }).then(({ body }) => {
          effectif = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/effectifs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/effectifs?page=0&size=20>; rel="last",<http://localhost/api/effectifs?page=0&size=20>; rel="first"',
              },
              body: [effectif],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(effectifPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Effectif page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('effectif');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', effectifPageUrlPattern);
      });

      it('edit button click should load edit Effectif page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Effectif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', effectifPageUrlPattern);
      });

      it('edit button click should load edit Effectif page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Effectif');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', effectifPageUrlPattern);
      });

      it('last delete button click should delete instance of Effectif', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('effectif').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', effectifPageUrlPattern);

        effectif = undefined;
      });
    });
  });

  describe('new Effectif page', () => {
    beforeEach(() => {
      cy.visit(`${effectifPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Effectif');
    });

    it('should create an instance of Effectif', () => {
      cy.get(`[data-cy="name"]`).type("d'avec ouin");
      cy.get(`[data-cy="name"]`).should('have.value', "d'avec ouin");

      cy.get(`[data-cy="cumul"]`).type('132');
      cy.get(`[data-cy="cumul"]`).should('have.value', '132');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        effectif = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', effectifPageUrlPattern);
    });
  });
});
