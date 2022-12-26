import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/PriceBox.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import Button from "../common/Button";

import '../../Btn.css'

const PriceBox = () => {
    return ( //
        <div className={styles.priceBox}>
            <div className={styles.priceList}>
                <button className="priceBtn">"5천원~1만원"</button>
                <button className="priceBtn">"1만원~3만원"</button>
                <button className="priceBtn">"3만원~10만원"</button>
                <button className="priceBtn">"10만원~20만원"</button>
                <button className="priceBtn">"20만원~30만원"</button>
                <button className="priceBtn">"30만원~50만원"</button>
                <button className="priceBtn">"50만원~100만원"</button>
                <button className="priceBtn">"100만원~"</button>
                {/*<Button content={"5천원~1만원"}/>*/}
                {/*<Button content={"1만원~3만원"}/>*/}
                {/*<Button content={"3만원~10만원"}/>*/}
                {/*<Button content={"10만원~20만원"}/>*/}
                {/*<Button content={"20만원~30만원"}/>*/}
                {/*<Button content={"30만원~50만원"}/>*/}
                {/*<Button content={"50만원~100만원"}/>*/}
                {/*<Button content={"100만원~"}/>*/}
            </div>
            <div className={styles.priceSet}>
                <input type="text" className={styles.setBox1}></input>
                <div className={styles.won}>원~</div>
                <input type="text" className={styles.setBox2}></input>
                <div className={styles.won}>원</div>

            </div>
        </div>
    );
}

export default PriceBox;