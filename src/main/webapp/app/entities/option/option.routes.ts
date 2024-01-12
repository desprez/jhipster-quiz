import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OptionComponent } from './list/option.component';
import { OptionDetailComponent } from './detail/option-detail.component';
import OptionResolve from './route/option-routing-resolve.service';

const optionRoute: Routes = [
  {
    path: '',
    component: OptionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OptionDetailComponent,
    resolve: {
      option: OptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default optionRoute;
