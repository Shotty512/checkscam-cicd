export class HandleReportDTO {
    idReport: number;
    status: number;
    idScamTypeAfterHandle: number

    constructor(data: any) {
        this.idReport = data.idReport;
        this.status = data.status;
        this.idScamTypeAfterHandle = data.idScamTypeAfterHandle;
    }
}