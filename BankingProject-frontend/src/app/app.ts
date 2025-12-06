import { Component, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { routes } from './app.routes';
import { provideRouter } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { Navbar } from './shared/navbar/navbar';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, HttpClientModule, Navbar],
  templateUrl: './app.html',
  styleUrls: ['./app.scss'],
})
export class App {
  title = signal('Banking Frontend');
}
