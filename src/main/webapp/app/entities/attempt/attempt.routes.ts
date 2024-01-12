import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AttemptComponent } from './list/attempt.component';
import { AttemptDetailComponent } from './detail/attempt-detail.component';
import { AttemptUpdateComponent } from './update/attempt-update.component';
import AttemptResolve from './route/attempt-routing-resolve.service';

const attemptRoute: Routes = [
  {
    path: '',
    component: AttemptComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttemptDetailComponent,
    resolve: {
      attempt: AttemptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttemptUpdateComponent,
    resolve: {
      attempt: AttemptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttemptUpdateComponent,
    resolve: {
      attempt: AttemptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default attemptRoute;
