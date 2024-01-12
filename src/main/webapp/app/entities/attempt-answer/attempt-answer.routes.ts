import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AttemptAnswerComponent } from './list/attempt-answer.component';
import { AttemptAnswerDetailComponent } from './detail/attempt-answer-detail.component';
import AttemptAnswerResolve from './route/attempt-answer-routing-resolve.service';

const attemptAnswerRoute: Routes = [
  {
    path: '',
    component: AttemptAnswerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttemptAnswerDetailComponent,
    resolve: {
      attemptAnswer: AttemptAnswerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default attemptAnswerRoute;
