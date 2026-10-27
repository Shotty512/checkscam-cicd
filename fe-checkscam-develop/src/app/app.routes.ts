import { Routes } from '@angular/router';
import { LayoutComponent } from './components/layout/layout.component';
import { UserComponent } from './components/user/user.component';
import { NewsComponent } from './components/news/news.component';
import { ReportComponent } from './components/report/report.component';
import { CreateNewsComponent } from './components/news/create-news/create-news.component';
import { ChatbotComponent } from './components/chatbot/chatbot.component';
import { LoginComponent } from './components/login/login.component';
import { CreateReportComponent } from './components/report/create-report/create-report.component';
import { ViewNewsComponent } from './components/news/view-news/view-news.component';
import { CreateUserComponent } from './components/user/create-user/create-user.component';
import { DetailNewsComponent } from './components/news/detail-news/detail-news.component';
import { DetailReportComponent } from './components/report/detail-report/detail-report.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
// import { ChatBoxComponent } from './components/chatbot/chat-box/chat-box.component';
import { AboutUsComponent } from './components/about-us/about-us.component'; 
import { ChatBoxComponent } from './components/chat-box/chat-box.component';
import { ListNewsComponent } from './components/news/list-news/list-news.component';
import { UpdateNewsComponent } from './components/news/update-news/update-news.component';


export const routes: Routes = [
    {
        path: '',
        component: LayoutComponent,
        children: [
          { path: 'users', component: UserComponent },
          { path: 'create-user', component: CreateUserComponent },
          { path: 'create-news', component: CreateNewsComponent },
          { path: 'news', component: NewsComponent },
          { path: 'reports', component: ReportComponent },
          { path: 'dashboard', component: DashboardComponent },
          { path: 'detail-news/:id', component: DetailNewsComponent }, 
          { path: 'update-news/:id', component: UpdateNewsComponent }, 
          { path: '', redirectTo: '/chatbot', pathMatch: 'full' },
        ],
      },
      { path: 'chatbot', component: ChatbotComponent}, 
      { path: 'chatbox', component: ChatBoxComponent },
      { path: 'login', component: LoginComponent }, 
      { path: 'create-report', component: CreateReportComponent }, 
      { path: 'list-news', component: ListNewsComponent }, 
      { path: 'detail-report/:id', component: DetailReportComponent }, 
      { path: 'about-us', component: AboutUsComponent },
      { path: 'view-news/:id', component: ViewNewsComponent },
      { path: '**', redirectTo: '/chatbot' },
];

