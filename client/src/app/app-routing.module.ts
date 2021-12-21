import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";
import {IndexComponent} from "./layout/index/index.component";
import {AuthGuardService} from "./helper/auth-guard.service";
import {ProfileComponent} from "./user/profile/profile.component";
import {UserBricksetsComponent} from "./user/user-bricksets/user-bricksets.component";
import {AddBricksetComponent} from "./user/add-brickset/add-brickset.component";

const routes: Routes = [ //Отвечает за то, что браузер будет показывать при посещении разных URL
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'main', component: IndexComponent, canActivate: [AuthGuardService]},
  {
    path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService], children: [ //Компоненты внутри компонента
      {path: '', component: UserBricksetsComponent, canActivate: [AuthGuardService]},
      {path: 'add', component: AddBricksetComponent, canActivate: [AuthGuardService]}
    ]
  },
  {path: '', redirectTo: 'main', pathMatch: 'full'} //Перекидываем на главную при выборе любого несуществующего URL
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
