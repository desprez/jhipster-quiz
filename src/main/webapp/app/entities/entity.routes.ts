import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'quizz',
    data: { pageTitle: 'quizzApp.quizz.home.title' },
    loadChildren: () => import('./quizz/quizz.routes'),
  },
  {
    path: 'question',
    data: { pageTitle: 'quizzApp.question.home.title' },
    loadChildren: () => import('./question/question.routes'),
  },
  {
    path: 'option',
    data: { pageTitle: 'quizzApp.option.home.title' },
    loadChildren: () => import('./option/option.routes'),
  },
  {
    path: 'attempt',
    data: { pageTitle: 'quizzApp.attempt.home.title' },
    loadChildren: () => import('./attempt/attempt.routes'),
  },
  {
    path: 'attempt-answer',
    data: { pageTitle: 'quizzApp.attemptAnswer.home.title' },
    loadChildren: () => import('./attempt-answer/attempt-answer.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
