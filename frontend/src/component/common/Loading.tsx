import React from "react";
import {ClipLoader, PacmanLoader} from "react-spinners";
import styles from "../../styles/common/Loading.module.css"

const Loading = () => {
    return (
        <div>
            <ClipLoader className={styles.loading}
                        color="#36d7b7"
                        size={50}
            />
            {/*<PacmanLoader color="#36d7b7"/>*/}
        </div>
    );
};

export default Loading;