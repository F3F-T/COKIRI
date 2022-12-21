import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/trade/Trade.module.css"

import {Outlet, useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../img/cokkiriLogo.png"
import PriceBox from "../component/trade/PriceBox";
import PostContainer from "../component/trade/PostContainer";
import TradeCategory from "../component/trade/TradeCategory";
import {useOutletContext} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";
import {decrease, increase, increaseByAmount} from "../store/storeReducer";



const MulmulTrade = () => {
    let navigate = useNavigate();

    //index에서 선언해준 Rootstate를 state로 받는다, store에 저장한다.
    const store = useSelector((state:Rootstate) => state);
    //action을 사용하기 위해 dispatch를 선언한다.
    const dispatch = useDispatch();

    return (
            <div className={styles.mulmulTrade}>
                <div className={styles.mulmulTradeContent}>
                    <div className={styles.categoryBox}>
                        <div className={styles.forWho}>
                            <button className={styles.forWho_1} onClick={()=>navigate('/mulmultrade')}>이런 물건이 올라왔어요.</button>
                            <button className={styles.forWho_1} onClick={()=>navigate('/mulmultrade/mulmultrade2')}>이런 물건을 원해요.</button>
                        </div>
                        <TradeCategory/>
                        <PriceBox/>
                    </div>

                    {/*부모 라우터인 mulmultrade2가 랜더링 될 부분을 outlet으로 지정해준다.*/}
                    <Outlet/>
                </div>
            </div>
    );
}

export default MulmulTrade;