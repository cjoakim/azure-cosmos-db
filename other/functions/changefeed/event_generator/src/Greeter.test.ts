import { Greeter } from "./Greeter";

test("greet", () => {
    let g = new Greeter();
    let s = g.greet('Miles') 
    let expected = 'hello Miles!';
    expect(s).toBe(expected);
});
