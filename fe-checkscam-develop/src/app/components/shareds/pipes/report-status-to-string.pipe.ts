import { Pipe, PipeTransform } from '@angular/core';
import { ReportStatus } from '../enums/report-status.enum';

@Pipe({
  name: 'reportStatusToString'
})
export class ReportStatusToStringPipe implements PipeTransform {

  transform(value: ReportStatus): string {
    switch (value) {
      case ReportStatus.PENDING:
        return 'Chờ xử lý';
      case ReportStatus.APPROVED:
        return 'Đã xác nhận';
      case ReportStatus.REJECTED:
        return 'Đã từ chối';
      default:
        return 'Trạng thái không xác định';
    }
  }

}