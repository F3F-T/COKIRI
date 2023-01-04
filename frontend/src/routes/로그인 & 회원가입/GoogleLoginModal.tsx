import styles from '../../styles/loginAndSignup/googleLoginModal.module.css'
import React, { PropsWithChildren } from "react";

interface ModalDefaultType {
    onClickToggleModal: () => void;
}

function Modal({
                   onClickToggleModal,
                   children,
               }: PropsWithChildren<ModalDefaultType>) {
    return (
        <div className={styles.box1}>
            <div className={styles.box2}>{children}</div>
            <div className={styles.box3}
                onClick={(e: React.MouseEvent) => {
                    e.preventDefault();
                    if (onClickToggleModal) {
                            onClickToggleModal();
                    }
                }}
            />
        </div>
    );
}
export default Modal