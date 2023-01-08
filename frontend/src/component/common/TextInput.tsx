import React, {useState, useEffect, useMemo, useCallback, InputHTMLAttributes, FC, ChangeEvent} from 'react';
import styles from "../../styles/common/TextInput.moudle.scss"
import classNames from "classnames/bind";

/**
 * 예시)
 * <TextInput placeholder={"코끼리 ID(이메일)을 입력해주세요."} onChange={handleChange}/>
 */

const cx = classNames.bind(styles)

interface TextInputProps{
    placeholder: string;
    value? : string;
    size? : string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onBlur? :(e: React.ChangeEvent<HTMLInputElement>) => void;
}

const TestInput = (textInputProps : TextInputProps) => {

    return (
        <>
            <div className={styles.inputText}>
                <input type="text" className={cx('inputText')} placeholder={textInputProps.placeholder} onChange={textInputProps.onChange} onBlur={textInputProps.onBlur} value={textInputProps.value}/>
            </div>
        </>
    );
}

export default TestInput;