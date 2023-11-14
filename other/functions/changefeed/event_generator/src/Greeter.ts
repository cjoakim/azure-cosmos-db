
export class Greeter {
    
    constructor(
        public verbose: boolean = false) {
        // no statements required
    }

    greet(name: string): string {
        return `hello ${name}!`;
    }

}
