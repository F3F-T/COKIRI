import React, {useState, useEffect, useMemo, useCallback, FC} from 'react';
import styles from "../../styles/common/Button.module.scss"
import classNames from "classnames/bind";
import exp from "constants";

/**
 * 예시
 * <Button className={"black"} onClick={handleClick} content={"코끼리 로그인"}/>
 */

const cx = classNames.bind(styles)

type ButtonTypes = 'black' | 'white' | 'lightblue'
type ButtonColor = "white" | "black"
type ButtonSize = "large" | "medium" | "small";

interface ButtonProps {
    className?: ButtonTypes;
    onClick?: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
    content: string;
    color?: string;
    hover?: boolean;
    size?: string;
}

/***
 * type ButtonTypes = "black" | "white"
 */
const Button = (buttonProps: ButtonProps) => {
    return (
        <>
            {buttonProps.className === undefined &&
                <button className={cx('defaultButton')} type={"button"}
                        onClick={buttonProps.onClick}>{buttonProps.content}
                </button>
            }

            {buttonProps.className === "black" &&
                <button className={cx('btnBlack')} type={"button"}
                        onClick={buttonProps.onClick}>{buttonProps.content}
                </button>
            }

            {buttonProps.className === "white" &&
                <button className={cx('btnWhite')} type={"button"}
                        onClick={buttonProps.onClick}>{buttonProps.content}
                </button>
            }

            {buttonProps.className === "lightblue" &&
                <button className={cx('btnLightBlue',
                    buttonProps.hover ? 'hoverLightBlueEnable' : '',
                    buttonProps.size
                )} type={"button"}
                        onClick={buttonProps.onClick}>{buttonProps.content}
                </button>
            }

        </>
    );
}






export default Button;
