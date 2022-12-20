import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/home/HomeMulmulTrade.module.css";
import Carousel from "react-bootstrap/Carousel";
import MulMulCardView from "./MulMulCardView";

const HomeMulmulTrade = () => {
    return (
        <section className={styles.mulmulTrade}>
            <h2>우리 동네 인기 물물교환</h2>
            <Carousel>
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
        </section>
    );
}


export default HomeMulmulTrade;
