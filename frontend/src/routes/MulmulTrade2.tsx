import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/trade/Trade.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../img/cokkiriLogo.png";
import TradeCategory from "../component/trade/TradeCategory";
import PriceBox from "../component/trade/PriceBox";
import PostContainer from "../component/trade/PostContainer";


const MulmulTrade2 = () => {
    let navigate = useNavigate();
    return (
            <div className={styles.mulmulTrade}>
                <div className={styles.mulmulTradeContent}>
                    <div className={styles.navPostOrWant}>도서를 원하는 사람들이에요.</div>
                    <div className={styles.popularOrNewest}>
                        <div className={styles.pupularBtn}>인기도순</div>
                        <div className={styles.slash}>|</div>
                        <div className={styles.newsetBtn}>누적도순</div>
                    </div>
                    <PostContainer/>
                </div>
            </div>
    );
}

export default MulmulTrade2;