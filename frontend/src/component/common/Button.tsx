import React, {useState, useEffect, useMemo, useCallback, FC} from 'react';
import styles from "../../styles/common/Button.module.scss"
import classNames from "classnames/bind";

/**
 * 예시
 * <Button className={"black"} onClick={handleClick} content={"코끼리 로그인"}/>
 */

const cx = classNames.bind(styles)


type ButtonTypes = "black" | "white"

interface ButtonProps {
    className?: ButtonTypes;
    onClick?: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
    content: string;
}

/***
 * type ButtonTypes = "black" | "white"
 */
const Button = (buttonProps: ButtonProps) => {
    return (
        <>

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

        </>
    );
}

export default Button;