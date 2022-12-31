import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/trade/Trade.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../img/cokkiriLogo.png"
import TradeCategory from "../component/trade/TradeCategory";
import PriceBox from "../component/trade/PriceBox";
import PostContainer from "../component/trade/PostContainer";


const KiriKiriTrade = () => {
    return (
        <div className={styles.mulmulTrade}>
            <div className={styles.mulmulTradeContent}>
                <div className={styles.categoryBox}>
                    <div className={styles.forWho}>
                        <button className={styles.forWho_1}>끼리끼리 교환해요.</button>
                    </div>
                    <TradeCategory/>
                    <PriceBox/>
                </div>
                <div className={styles.navPostOrWant}>도서끼리 교환해요</div>
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

export default KiriKiriTrade;