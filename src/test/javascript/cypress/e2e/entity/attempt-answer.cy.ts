import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('AttemptAnswer e2e test', () => {
  const attemptAnswerPageUrl = '/attempt-answer';
  const attemptAnswerPageUrlPattern = new RegExp('/attempt-answer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const attemptAnswerSample = { started: '2024-01-11T19:12:52.084Z' };

  let attemptAnswer;
  let question;
  let option;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/questions',
      body: { statement: 'uh-huh anti shocking', index: 5068, correctOptionIndex: 19617 },
    }).then(({ body }) => {
      question = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/options',
      body: { statement: 'where infuse', index: 5229 },
    }).then(({ body }) => {
      option = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/attempt-answers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/attempt-answers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/attempt-answers/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/questions', {
      statusCode: 200,
      body: [question],
    });

    cy.intercept('GET', '/api/options', {
      statusCode: 200,
      body: [option],
    });

    cy.intercept('GET', '/api/attempts', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (attemptAnswer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/attempt-answers/${attemptAnswer.id}`,
      }).then(() => {
        attemptAnswer = undefined;
      });
    }
  });

  afterEach(() => {
    if (question) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/questions/${question.id}`,
      }).then(() => {
        question = undefined;
      });
    }
    if (option) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/options/${option.id}`,
      }).then(() => {
        option = undefined;
      });
    }
  });

  it('AttemptAnswers menu should load AttemptAnswers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('attempt-answer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AttemptAnswer').should('exist');
    cy.url().should('match', attemptAnswerPageUrlPattern);
  });

  describe('AttemptAnswer page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(attemptAnswerPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AttemptAnswer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('attemptAnswer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', attemptAnswerPageUrlPattern);
      });
    });
  });
});
