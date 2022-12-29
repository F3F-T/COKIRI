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
import Button from "../component/common/Button";

//모르는 태그가 너무 많아 하다가 멈춤
//허락 맡고 다시 진행 예정
const HomeStart = () => {
    const navigate = useNavigate();


    const onClickStart = () => {
        navigate(`/login`)
    }

    const onClickUpload = () => {
        navigate(`/upload`)
    }
    return (
        <section className={styles.start}>
            <div className={styles.startLeft}>CO끼리</div>
            <div className={styles.startRight}>
                <div className={styles.startRight1}>
                    중고 거래부터 동네 인증까지, 코끼리와 함께해요.<br/>
                    가볍고 따뜻한 코끼리를 만들어요.</div>
                <div className={styles.startRight2}>

                    <Button className={"lightblue"} content={"시작하기"} onClick={onClickStart} color={"black"} hover={true} size={"medium"}/>
                    <Button className={"lightblue"} content={"내 물건 올리기"} onClick={onClickUpload} color={"black"} hover={true} size={"medium"}/>

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
    return (
        <div className={styles.home}>
        <HomeStart/>
        <HomeMulmulTrade/>
        <HomeKirikiriTrade/>
        </div>
    );
}

export default Home;