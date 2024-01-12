import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('Option e2e test', () => {
  const optionPageUrl = '/option';
  const optionPageUrlPattern = new RegExp('/option(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const optionSample = { statement: 'needily coolly', index: 5797 };

  let option;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/options+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/options').as('postEntityRequest');
    cy.intercept('DELETE', '/api/options/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (option) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/options/${option.id}`,
      }).then(() => {
        option = undefined;
      });
    }
  });

  it('Options menu should load Options page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('option');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Option').should('exist');
    cy.url().should('match', optionPageUrlPattern);
  });

  describe('Option page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(optionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Option page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('option');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', optionPageUrlPattern);
      });
    });
  });
});
