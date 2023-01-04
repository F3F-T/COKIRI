import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/Message.module.css"
import classNames from "classnames/bind";

const cx = classNames.bind(styles)

interface MessageProps {
    validCheck : undefined | boolean;
    content : string;
}
const Message = (messageProps : MessageProps) => {
    return (
        <>
            {messageProps.validCheck === undefined && null}

            {messageProps.validCheck === true &&
                <p className={cx('successMessage')}>
                    {messageProps.content}
                </p>
            }

            {messageProps.validCheck === false &&
                <p className={cx('errorMessage')}>
                    {messageProps.content}
                </p>
            }
        </>
    );
}

export default Message;