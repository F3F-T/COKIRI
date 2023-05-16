import React from 'react';
import styles from '../../styles/talk/talkList.module.scss';
import { useNavigate } from 'react-router-dom';
import transfer from '../../img/transfer.png';

interface TalkCardProps {
  talkCardInfo: TalkCardInfo;
}

interface TalkCardInfo {
  productImg: string,
  tradeStatus: string,
  title: string,
  tradeCategory: string,
  wishCategory: string
  postId: number,
  price: number,
  authorId: number
}

const TalkCard2 = ({ talkCardInfo }: TalkCardProps) => {
  const navigate = useNavigate();

  const onClickPost = () => {
    navigate(`/post/${talkCardInfo.postId}`)
  }

  return (
    <>
      <img className={styles.cardImage} onClick={onClickPost} src={talkCardInfo.productImg} />
      <div className={styles.productInfo}>
        <p className={styles.tradeState}>{talkCardInfo.tradeStatus}</p>
        <p className={styles.cardTitle}> {talkCardInfo.title} </p>
        <div className={styles.tradeBox}>
          <p className={styles.cardCategory}> {talkCardInfo.tradeCategory} </p>
          <img className={styles.tradeimoticon} src={transfer} />
          <p className={styles.cardCategory}> {talkCardInfo.wishCategory} </p>
        </div>
      </div>

    </>
  );
};
export default TalkCard2;