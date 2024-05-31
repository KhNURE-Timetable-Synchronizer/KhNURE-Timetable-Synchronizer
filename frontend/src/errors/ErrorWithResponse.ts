export class ErrorWithResponse extends Error {
    response: Response

    constructor(message: string, response: Response) {
        super(message); // (1)
        this.name = "ErrorWithResponse"; // (2)
        this.response = response;
    }
}