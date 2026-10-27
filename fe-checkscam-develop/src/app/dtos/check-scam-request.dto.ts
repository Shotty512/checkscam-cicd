export class CheckScamRequestDTO {
    info: string;
    type: number;

    constructor(data: any) {
        this.info = data.info;
        this.type = data.type;
    }
}