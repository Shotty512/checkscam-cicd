import { Pipe, PipeTransform } from '@angular/core';
import { InformationType } from '../enums/information-type.enum';

@Pipe({
  name: 'informationTypeToString',
  standalone: true
})
export class InformationTypeToStringPipe implements PipeTransform {
  transform(value: InformationType): string {
    switch (value) {
      case InformationType.PhoneNumber: return 'Số điện thoại';
      case InformationType.Bank: return 'Số tài khoản';
      case InformationType.URL: return 'URL';
      default: return 'Loại thông tin không xác định';
    }
  }
}
