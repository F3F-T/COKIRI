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
import {resetCategory} from "../store/categoryReducer";
import Button from "../component/common/Button";
import classNames from "classnames/bind";


const MulmulTrade = () => {
    let navigate = useNavigate();
    const cx = classNames.bind(styles)

    //index에서 선언해준 Rootstate를 state로 받는다, store에 저장한다.
    const store = useSelector((state: Rootstate) => state);
    //action을 사용하기 위해 dispatch를 선언한다.
    const dispatch = useDispatch();
    const [isClicked,setIsClicked] = useState<boolean>(true);

    /**
     * 랜더링될때 category를 도서로 다시 초기화시킨다.
     * 이는 끼리끼리, 물물교환으로 페이지를 이동할때 도서로 초기화 시키는 역할을 함
     * */
    useEffect(() => {
        return () => {
            dispatch(resetCategory());
        };
    }, []);

    const onClickProductButton = () => {
        setIsClicked(true);
        navigate('/mulmultrade');
    }

    const onClickWishButton = () => {
        setIsClicked(false);
        navigate('/mulmultrade/mulmultrade2');
    }



    return (
        <div className={styles.mulmulTrade}>
            <div className={styles.mulmulTradeContent}>
                <div className={styles.categoryBox}>
                    <div className={styles.forWho}>
                        <div className={styles.buttonMargin}>
                            <Button className={cx("lightblue")} content={"이런 물건이 올라왔어요"}
                                    onClick={onClickProductButton} hover={true} size={"small"}/>
                        </div>
                        <Button className={cx("lightblue")} content={"이런 물건을 원해요"}
                                onClick={onClickWishButton} hover={true} size={"small"}/>
                    </div>
                    <TradeCategory/>
                    <PriceBox/>
                </div>
                {/*자식 라우터인 mulmultrade1, mulmultrade2가 랜더링 될 부분을 outlet으로 지정해준다.*/}
                <Outlet/>
            </div>
        </div>
    );
}

export default MulmulTrade;