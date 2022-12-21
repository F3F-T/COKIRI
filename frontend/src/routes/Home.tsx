import React, {useState, useEffect, useMemo, useCallback} from 'react';
import MulMulCardView from "../component/home/MulMulCardView";
import Carousel from 'react-bootstrap/Carousel';
import KiriKiriCategoryRoundImage from "../component/home/KiriKiriCategoryRoundImage";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import {useNavigate} from "react-router-dom";
import styles from "../styles/home/Home.module.scss";
import book from "../img/book.png";
import fashion from "../img/fashion.png";
import ticket from "../img/ticket.png";
import young from "../img/young.png";
import {decrease, increase, increaseByAmount} from "../store/counterReducer";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";


const HomeStart = () => {
    const navigate = useNavigate();


    const onClickStart = () => {
        navigate(`/login`)
    }
    return (
        <section className={styles.start}>
            <div className={styles.startLeft}>CO끼리</div>
            <div className={styles.startRight}>
                <div className={styles.startRight1}>
                    중고 거래부터 동네 인증까지, 코끼리와 함께해요.<br/>
                    가볍고 따뜻한 코끼리를 만들어요.</div>
                <div className={styles.startRight2}>
                    <button onClick={onClickStart}>시작하기</button>
                    <button>내 물건 올리기</button>
                </div>
            </div>
        </section>
    );
}

const directionButtons = (direction) => {
    return (
        <span
            aria-hidden="true"
            className={direction === "Next" ? "button-next" : "button-prev"}
        >
        {direction}
      </span>
    );
};

const HomeMulmulTrade = () => {
    return (
        <section className={styles.mulmulTrade}>
            <h2>우리 동네 인기 물물교환</h2>

                    <div className={styles.mulmulCardView}>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <div>
                        </div>
                    </div>



                    {/*<div className={styles.mulmulCardView}>*/}
                    {/*    <MulMulCardView/>*/}
                    {/*    <MulMulCardView/>*/}
                    {/*    <MulMulCardView/>*/}
                    {/*    <MulMulCardView/>*/}
                    {/*</div>*/}

        </section>
    );
}

const HomeKirikiriTrade = () => {
    return (
        <section className={styles.kirikiriTrade}>
            <h2>다른 카테고리 뿐만 아니라 같은 카테고리 끼리도 교환할 수 있어요</h2>

            <div className={styles.kirikiriCatagoryCardView}>
                <Row>
                    <Col xs={3}>
                        <KiriKiriCategoryRoundImage props={book}/>
                    </Col>
                    <Col xs={3}>
                        <KiriKiriCategoryRoundImage props={fashion}/>
                    </Col>
                    <Col xs={3}>
                        <KiriKiriCategoryRoundImage props={ticket}/>
                    </Col>
                    <Col xs={3}>
                        <KiriKiriCategoryRoundImage props={young}/>
                    </Col>
                </Row>
            </div>;


            <div className ={styles.carou}>

            <Carousel nextLabel={"Next"}
                      prevLabel={"Previous"}
                      nextIcon={directionButtons("Next")}
                      prevIcon={directionButtons("Previous")}
                      variant={"dark"}
            >
                <Carousel.Item interval={5000}>
                    <div className={styles.mulmulCardView}>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <div>
                        </div>
                    </div>
                </Carousel.Item>

                <Carousel.Item interval={5000}>
                    <div className={styles.mulmulCardView}>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                    </div>
                </Carousel.Item>
            </Carousel>
            </div>
        </section>
    );
}

const Home = () => {
    //index에서 선언해준 Rootstate를 state로 받는다, store에 저장한다.
    const store = useSelector((state:Rootstate) => state);
    //action을 사용하기 위해 dispatch를 선언한다.
    const dispatch = useDispatch();

    return (
        <div className={styles.home}>

            <p>mulmultrade{store.counter.count}</p>
            <button onClick = {()=> {dispatch(increase())}}>Up</button>
            <button onClick = {()=> {dispatch(decrease())}}>down</button>
            <button onClick = {()=> {dispatch(increaseByAmount())}}>amount</button>
        <HomeStart/>
        <HomeMulmulTrade/>
        <HomeKirikiriTrade/>
        </div>
    );
}

export default Home;