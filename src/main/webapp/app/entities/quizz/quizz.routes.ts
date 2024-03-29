import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QuizzComponent } from './list/quizz.component';
import { QuizzDetailComponent } from './detail/quizz-detail.component';
import { QuizzUpdateComponent } from './update/quizz-update.component';
import QuizzResolve from './route/quizz-routing-resolve.service';
import { QuizzPlayComponent } from './play/quizz-play.component';
import { ModalContainerComponent } from './play/modal-container-routable-modals';

const quizzRoute: Routes = [
  {
    path: '',
    component: QuizzComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuizzDetailComponent,
    resolve: {
      quizz: QuizzResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuizzUpdateComponent,
    resolve: {
      quizz: QuizzResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuizzUpdateComponent,
    resolve: {
      quizz: QuizzResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/play',
    component: ModalContainerComponent,
    resolve: {
      quizz: QuizzResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default quizzRoute;
