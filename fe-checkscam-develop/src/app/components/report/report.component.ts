  import { CommonModule } from '@angular/common';
  import { Component, OnInit } from '@angular/core';
  import { RouterModule } from '@angular/router';
  import { FormsModule } from '@angular/forms';

  import { ReportService } from '../../services/report.service';
  import { ScamTypeService, ScamTypeDto } from '../../services/scam-type.service';

  import { ReportStatusToStringPipe } from '../shareds/pipes/report-status-to-string.pipe';
  import { InformationTypeToStringPipe } from '../shareds/pipes/information-type-to-string.pipe';

  @Component({
    selector: 'app-report',
    standalone: true,
    imports: [
      CommonModule,
      RouterModule,
      FormsModule,                  
      ReportStatusToStringPipe,
      InformationTypeToStringPipe
    ],
    templateUrl: './report.component.html',
    styleUrls: ['./report.component.scss']
  })
  export class ReportComponent implements OnInit {

    /* ------------ dữ liệu bảng ------------ */
    reports: any[] = [];

    /* ------------ state dropdown ------------ */
    dropdownOpenForId: number | null = null;     // id report đang mở
    scamTypes: ScamTypeDto[] = [];
    selectedScamTypeId: number | null = null;
    newScamType = { name: '', code: '' };

    constructor(
      private reportService: ReportService,
      private scamTypeService: ScamTypeService
    ) { }

    /* ===== lifecycle ===== */
    ngOnInit(): void { this.loadAllReports(); }

    /* ===== gọi API danh sách ===== */
    loadAllReports(): void {
      this.reportService.getListReports().subscribe({
        next: (res) => this.reports = res.data || [],
        error: (err) => alert(err.error?.message || err.message || 'Lỗi tải danh sách')
      });
    }

    /* ===== mở dropdown ===== */
    openDropdown(reportId: number): void {
      this.dropdownOpenForId = reportId;
      this.selectedScamTypeId = null;
      this.newScamType = { name: '', code: '' };

      if (this.scamTypes.length === 0) {
        this.scamTypeService.getAll().subscribe({
          next: list => this.scamTypes = list,
          error: () => alert('Không tải được danh sách hình thức lừa đảo')
        });
      }
    }

    closeDropdown(): void { this.dropdownOpenForId = null; }

    /* ===== xác nhận với id có sẵn ===== */
    confirmReport(reportId: number): void {
      if (!this.selectedScamTypeId) return;

      this.reportService.handleReport({
        idReport: reportId,
        status: 2,
        idScamTypeAfterHandle: this.selectedScamTypeId
      }).subscribe({
        next: () => { alert('Xác nhận thành công'); this.afterHandle(); },
        error: () => alert('Lỗi xác nhận')
      });
    }

    /* ===== tạo mới + xác nhận ===== */
    createScamTypeAndConfirm(reportId: number): void {
      const { name, code } = this.newScamType;
      if (!name.trim() || !code.trim()) { alert('Nhập tên & mã!'); return; }

      this.scamTypeService.create({ name: name.trim(), code: code.trim() }).subscribe({
        next: (created) => {
          this.scamTypes.push(created);
          this.selectedScamTypeId = created.id;
          this.confirmReport(reportId);
        },
        error: () => alert('Lỗi tạo mới Scam‑type')
      });
    }

    /* ===== từ chối ===== */
    rejectReport(reportId: number): void {
      this.reportService.handleReport({
        idReport: reportId,
        status: 3,
        idScamTypeAfterHandle: null
      }).subscribe({
        next: () => { alert('Từ chối thành công'); this.afterHandle(); },
        error: () => alert('Lỗi từ chối')
      });
    }

    private afterHandle(): void {
      this.closeDropdown();
      this.loadAllReports();
    }
  }
