import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/PriceBox.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import Button from "../common/Button";

import '../../Btn.css'
import {resetPrice, setPrice} from "../../store/priceReducer";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";
import {current} from "@reduxjs/toolkit";
import classNames from "classnames/bind";
const cx = classNames.bind(styles)

const prices: string[] =
    ['5천원~1만원', '1만원~3만원', '3만원~10만원', '10만원~20만원', '20만원~30만원', '30만원~50만원', '50만원~100만원', '100만원~']
const PriceBox = () => {


    const [isClicked,setIsClicked] = useState();
    const store = useSelector((state:Rootstate) => state);
    //action을 사용하기 위해 dispatch를 선언한다.
    const dispatch = useDispatch();

    interface priceJson {
        minPrice: string,
        maxPrice: string,
    }
    let priceJson:priceJson;

    const onClickPriceButton = (e,price) => {
        let minPrice: string = "";
        let maxPrice: string = "";

        setIsClicked((prev) => {
            return e.target.value;
        });

        console.log(isClicked);

        if (price === '5천원~1만원') {
             priceJson =
                {
                    minPrice: "5000",
                    maxPrice: "10000"
                }
        }
        else if (price === '1만원~3만원') {
            priceJson =
                {
                    minPrice: "10000",
                    maxPrice: "30000"
                }
        }
        else if (price === '3만원~10만원') {
            priceJson =
                {
                    minPrice: "30000",
                    maxPrice: "100000"
                }
        }
        else if (price === '10만원~20만원') {
            priceJson =
                {
                    minPrice: "100000",
                    maxPrice: "200000"
                }
        }
        else if (price === '20만원~30만원') {
            priceJson =
                {
                    minPrice: "200000",
                    maxPrice: "300000"
                }
        }
        else if (price === '30만원~50만원') {
            priceJson =
                {
                    minPrice: "300000",
                    maxPrice: "500000"
                }
        }
        else if (price === '50만원~100만원') {
            priceJson =
                {
                    minPrice: "500000",
                    maxPrice: "1000000"
                }
        }
        else if (price === '100만원~') {
            priceJson =
                {
                    minPrice: "1000000",
                    maxPrice: ""
                }
        }

        //store에 값이 존재하면 초기화, store에 값이 존재하지 않으면 누른 값으로 dispatch

        if(store.priceReducer.minPrice || store.priceReducer.maxPrice){
            dispatch(resetPrice());
        }else
        {
            dispatch(setPrice(priceJson));
        }
    }

    return (
        <div className={styles.priceBox}>
            <div className={styles.priceList}>
                {prices.map((price: string, index) => (
                    <button value = {index} className={cx(`priceBtn`, index != isClicked ? `` : `red`)} onClick={(event) => onClickPriceButton(event,price)}>{price}</button>
                ))}

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