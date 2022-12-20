//이렇게 선언을 해주어 css module을 import할 수 있게끔 한다.
declare module "*.css" {
    const content: { [className: string]: string };
    export = content;
}

declare module '*.scss'
declare module '*.png';
declare module '*.jpeg';