import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgChartsModule } from 'ng2-charts';
import {
  ChartOptions,
  ChartType,
  ChartData,
  ChartDataset
} from 'chart.js';
import { ReportService } from '../../services/report.service';

interface YearlyStat { year: number; count: number; }
interface MonthlyStat { month: number; count: number; }

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [ CommonModule, FormsModule, NgChartsModule ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  public yearlyChartType: ChartType = 'bar';
  public yearlyChartData!: ChartData<'bar'>;
  public yearlyChartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: { callbacks: { label: ctx => `Báo cáo: ${ctx.parsed.y}` } }
    },
    scales: {
      x: { title: { display: true, text: 'Năm' }, grid: { display: false }, border: { display: false } },
      y: { title: { display: true, text: 'Số báo cáo' }, beginAtZero: true, ticks: { stepSize: 1 }, grid: { color: 'rgba(0,0,0,0.05)' }, border: { display: false } }
    }
  };

  public monthlyChartType: ChartType = 'line';
  public monthlyChartData!: ChartData<'line'>;
  public monthlyChartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: { callbacks: { label: ctx => `Báo cáo: ${ctx.parsed.y}` } }
    },
    scales: {
      x: { title: { display: true, text: 'Tháng' }, grid: { display: false }, border: { display: false } },
      y: { title: { display: true, text: 'Số báo cáo' }, beginAtZero: true, ticks: { stepSize: 1 }, grid: { display: true, color: 'rgba(0,0,0,0.1)', lineWidth: 1, drawTicks: false }, border: { display: false } }
    }
  };

  public availableYears: number[] = [];
  public selectedYear!: number;

  constructor(private reportService: ReportService) {}

  ngOnInit(): void {
    this.reportService.getYearlyStats().subscribe((stats: YearlyStat[]) => {
      stats.sort((a, b) => a.year - b.year);

      this.availableYears = stats.map(s => s.year);
      this.selectedYear = this.availableYears[this.availableYears.length - 1];

      const recentStats = stats.slice(-5);
      this.loadYearlyChart(recentStats);

      this.loadMonthlyChart(this.selectedYear);
    });
  }

  private loadYearlyChart(stats: YearlyStat[]): void {
    const labels = stats.map(s => s.year.toString());
    const data = stats.map(s => s.count);
    const ds: ChartDataset<'bar'> = { data, label: 'Số báo cáo', backgroundColor: '#38bdf8', borderColor: '#0ea5e9', borderWidth: 1, borderRadius: 4 };
    this.yearlyChartData = { labels, datasets: [ds] };
  }

  public loadMonthlyChart(year: number): void {
    this.reportService.getMonthlyStats(year).subscribe((stats: MonthlyStat[]) => {
      const map = new Map<number, number>();
      stats.forEach(s => map.set(s.month, s.count));

      const labels: string[] = [];
      const data: number[] = [];
      for (let m = 1; m <= 12; m++) {
        labels.push(`Tháng ${m}`);
        data.push(map.get(m) ?? 0);
      }

      const ds: ChartDataset<'line'> = { data, label: 'Số báo cáo', fill: false, borderColor: '#38bdf8', tension: 0.3, pointBackgroundColor: '#38bdf8' };
      this.monthlyChartData = { labels, datasets: [ds] };
    });
  }

  public onYearChange(year: string): void {
    this.selectedYear = +year;
    this.loadMonthlyChart(this.selectedYear);
  }
}
