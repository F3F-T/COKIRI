import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/PriceBox.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

// ㅂㅅ
const PriceBox = () => {
    return (
        <div className={styles.priceBox}>
            <div className={styles.priceList}>
                <button className={styles.priceI}>5천원~1만원</button>
                <button className={styles.priceI}>1만원~3만원</button>
                <button className={styles.priceI}>3만원~10만원</button>
                <button className={styles.priceI}>10만원~20만원</button>
                <button className={styles.priceI}>20만원~30만원</button>
                <button className={styles.priceI}>30만원~50만원</button>
                <button className={styles.priceI}>50만원~100만원</button>
                <button className={styles.priceI}>100만원~</button>
            </div>
            <div className={styles.priceSet}>
                <input type="text" className={styles.setBox1}></input>
                <div className={styles.won}>~원</div>
                <input type="text" className={styles.setBox2}></input>
            </div>
        </div>
    );
}

export default PriceBox;