import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Quizz e2e test', () => {
  const quizzPageUrl = '/quizz';
  const quizzPageUrlPattern = new RegExp('/quizz(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const quizzSample = {"title":"or lazy anenst","difficulty":"MEDIUM","category":"BOOKS","questionOrder":"FIXED","allowBack":true,"allowReview":true,"secretGoodAnwers":true,"published":false};

  let quizz;
  // let user;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"closure thoroughly","firstName":"Constantin","lastName":"Connelly"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/quizzes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/quizzes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/quizzes/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/questions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (quizz) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quizzes/${quizz.id}`,
      }).then(() => {
        quizz = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('Quizzes menu should load Quizzes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('quizz');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Quizz').should('exist');
    cy.url().should('match', quizzPageUrlPattern);
  });

  describe('Quizz page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(quizzPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Quizz page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/quizz/new$'));
        cy.getEntityCreateUpdateHeading('Quizz');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizzPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/quizzes',
          body: {
            ...quizzSample,
            user: user,
          },
        }).then(({ body }) => {
          quizz = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/quizzes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/quizzes?page=0&size=20>; rel="last",<http://localhost/api/quizzes?page=0&size=20>; rel="first"',
              },
              body: [quizz],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(quizzPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(quizzPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Quizz page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('quizz');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizzPageUrlPattern);
      });

      it('edit button click should load edit Quizz page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Quizz');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizzPageUrlPattern);
      });

      it.skip('edit button click should load edit Quizz page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Quizz');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizzPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Quizz', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('quizz').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', quizzPageUrlPattern);

        quizz = undefined;
      });
    });
  });

  describe('new Quizz page', () => {
    beforeEach(() => {
      cy.visit(`${quizzPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Quizz');
    });

    it.skip('should create an instance of Quizz', () => {
      cy.get(`[data-cy="title"]`).type('attitude');
      cy.get(`[data-cy="title"]`).should('have.value', 'attitude');

      cy.get(`[data-cy="description"]`).type('traveler unwelcome');
      cy.get(`[data-cy="description"]`).should('have.value', 'traveler unwelcome');

      cy.get(`[data-cy="difficulty"]`).select('EASY');

      cy.get(`[data-cy="category"]`).select('SPORTS');

      cy.get(`[data-cy="questionOrder"]`).select('FIXED');

      cy.get(`[data-cy="maxAnswerTime"]`).type('12974');
      cy.get(`[data-cy="maxAnswerTime"]`).should('have.value', '12974');

      cy.get(`[data-cy="allowBack"]`).should('not.be.checked');
      cy.get(`[data-cy="allowBack"]`).click();
      cy.get(`[data-cy="allowBack"]`).should('be.checked');

      cy.get(`[data-cy="allowReview"]`).should('not.be.checked');
      cy.get(`[data-cy="allowReview"]`).click();
      cy.get(`[data-cy="allowReview"]`).should('be.checked');

      cy.get(`[data-cy="secretGoodAnwers"]`).should('not.be.checked');
      cy.get(`[data-cy="secretGoodAnwers"]`).click();
      cy.get(`[data-cy="secretGoodAnwers"]`).should('be.checked');

      cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="published"]`).should('not.be.checked');
      cy.get(`[data-cy="published"]`).click();
      cy.get(`[data-cy="published"]`).should('be.checked');

      cy.get(`[data-cy="user"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        quizz = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', quizzPageUrlPattern);
    });
  });
});
