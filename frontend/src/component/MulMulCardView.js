import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import myImage from "../img/cokkiriLogo.png"
import styles from "../styles/Home.module.css"
function MulMulCardView() {
    return (
        <Card className={styles.mulmulCardView}>
            <Card.Img className={styles.mulmulCardImg}
                variant="top" src={myImage} />
            <Card.Body>
                <div className={styles.mulmulCardDetail}>
                <div className={styles.mulmulCardViewTitle}>스팸 다른 식품이랑 바꾸실분</div>
                <div className={styles.mulmulCardViewContent}>말 그대로 나는 스팸이랑 바꾸려고해요</div>
                    <div className={styles.mulmulCardViewBottomDetail}>
                    <li>좋아요</li>
                    <li>댓글</li>
                    <li>가구</li>
                    </div>
                </div>
            </Card.Body>
        </Card>
    );
}

export default MulMulCardView;