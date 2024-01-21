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

describe('Attempt e2e test', () => {
  const attemptPageUrl = '/attempt';
  const attemptPageUrlPattern = new RegExp('/attempt(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const attemptSample = {"score":15292,"started":"2024-01-12T04:33:43.229Z"};

  let attempt;
  // let quizz;
  // let user;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/quizzes',
      body: {"title":"formal abaft unless","description":"survey circa","difficulty":"MEDIUM","category":"CELEBRITIES","questionOrder":"FIXED","maxAnswerTime":26004,"allowBack":true,"allowReview":true,"secretGoodAnwers":true,"image":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","imageContentType":"unknown","published":true},
    }).then(({ body }) => {
      quizz = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"warlike dull strictly","firstName":"Margie","lastName":"Luettgen"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/attempts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/attempts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/attempts/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/attempt-answers', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/quizzes', {
      statusCode: 200,
      body: [quizz],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (attempt) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/attempts/${attempt.id}`,
      }).then(() => {
        attempt = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (quizz) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quizzes/${quizz.id}`,
      }).then(() => {
        quizz = undefined;
      });
    }
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

  it('Attempts menu should load Attempts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('attempt');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Attempt').should('exist');
    cy.url().should('match', attemptPageUrlPattern);
  });

  describe('Attempt page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(attemptPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Attempt page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/attempt/new$'));
        cy.getEntityCreateUpdateHeading('Attempt');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', attemptPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/attempts',
          body: {
            ...attemptSample,
            quizz: quizz,
            user: user,
          },
        }).then(({ body }) => {
          attempt = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/attempts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/attempts?page=0&size=20>; rel="last",<http://localhost/api/attempts?page=0&size=20>; rel="first"',
              },
              body: [attempt],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(attemptPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(attemptPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Attempt page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('attempt');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', attemptPageUrlPattern);
      });

      it('edit button click should load edit Attempt page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Attempt');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', attemptPageUrlPattern);
      });

      it('edit button click should load edit Attempt page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Attempt');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', attemptPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Attempt', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('attempt').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', attemptPageUrlPattern);

        attempt = undefined;
      });
    });
  });

  describe('new Attempt page', () => {
    beforeEach(() => {
      cy.visit(`${attemptPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Attempt');
    });

    it.skip('should create an instance of Attempt', () => {
      cy.get(`[data-cy="score"]`).type('1664');
      cy.get(`[data-cy="score"]`).should('have.value', '1664');

      cy.get(`[data-cy="started"]`).type('2024-01-12T02:14');
      cy.get(`[data-cy="started"]`).blur();
      cy.get(`[data-cy="started"]`).should('have.value', '2024-01-12T02:14');

      cy.get(`[data-cy="ended"]`).type('2024-01-12T13:36');
      cy.get(`[data-cy="ended"]`).blur();
      cy.get(`[data-cy="ended"]`).should('have.value', '2024-01-12T13:36');

      cy.get(`[data-cy="quizz"]`).select(1);
      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        attempt = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', attemptPageUrlPattern);
    });
  });
});
