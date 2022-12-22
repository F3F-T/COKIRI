import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/Message.module.css"
import classNames from "classnames/bind";

const cx = classNames.bind(styles)

interface MessageProps {
    passwordCheck : undefined | boolean;
    content : string;
}
const message = (messageProps : MessageProps) => {
    return (
        <>
            {messageProps.passwordCheck === undefined && null}

            {messageProps.passwordCheck === true &&
                <p className={cx('successMessage')}>
                    {messageProps.content}
                </p>
            }

            {messageProps.passwordCheck === false &&
                <p className={cx('errorMessage')}>
                    {messageProps.content}
                </p>
            }
        </>
    );
}

export default message;