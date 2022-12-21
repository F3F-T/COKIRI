import React, {useState, useEffect, useMemo, useCallback, InputHTMLAttributes, FC, ChangeEvent} from 'react';
import styles from "../../styles/common/TextInput.moudle.scss"
import classNames from "classnames/bind";


const cx = classNames.bind(styles)

interface TextInputProps{
    placeholder: string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const TestInput: FC<TextInputProps> = ({placeholder,onChange, ...rest}) => {

    return (
        <>
            <div className={styles.inputText}>
                <input type="text" className={cx('inputText')} placeholder={placeholder} onChange={onChange} />
            </div>
        </>
    );
}

export default TestInput;