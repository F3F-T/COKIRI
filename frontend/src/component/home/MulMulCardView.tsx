import React from 'react';
import Button from 'react-bootstrap/Button';
// import Card from 'react-bootstrap/Card';
import myImage from "../../img/cokkiriLogo.png"
import styles from "../../styles/home/Home.module.scss"
import Card from "../tradeCard/Card";

function MulMulCardView() {
    return (
        // <Card className={styles.cardViewPlease}>
        //     <Card.Img className={styles.mulmulCardImg}
        //         variant="top" src={myImage}/>
        //     <Card.Body>
        //         <div className={styles.mulmulCardDetail}>
        //         <div className={styles.mulmulCardViewTitle}>스팸 다른 식품이랑 바꾸실분</div>
        //         <div className={styles.mulmulCardViewContent}>말 그대로 나는 스팸이랑 바꾸려고해요</div>
        //             <div className={styles.mulmulCardViewBottomDetail}>
        //             <li>좋아요</li>
        //             <li>댓글</li>
        //             <li>가구</li>
        //             </div>
        //         </div>
        //     </Card.Body>
        // </Card>
        <Card className={"forTrade"} postTitle={"21fw쿠어 MTR 발마칸 코트Msdfsdkbkhug"} postContent={"남성의류"} like={3} comment={5} wishCategory={"가구"} />
    );
}

export default MulMulCardView;