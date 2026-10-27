export class NewsDTO {
    name: string;
    shortDescription: string;
    content: string;

    constructor(data: any) {
        this.name = data.name;
        this.shortDescription = data.shortDescription;
        this.content = data.content;
    }
}