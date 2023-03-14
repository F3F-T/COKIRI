import React from 'react';
import styles from '../../styles/talk/talkList.module.scss';
import classNames from 'classnames/bind';
import { useSelector } from 'react-redux';
import { Rootstate } from '../../index';


// type clickOrNot = true | false ;
const tl = classNames.bind(styles);

interface TalkListProps {
  onClick?: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => any;
  partner: string;
  lastContent: string;
  date: string;
  keys?: number;
  counts?: number;
}

interface talkInfoType {
  partner: string;
  lastContent: string;
  date: string;
  keys?: number;
  counts?: number;
}


const TalkList2 = (talkListProps: TalkListProps) => {
  const store = useSelector((state: Rootstate) => state);

  return (
    <>
      <div className={styles.talkContent}>
        <button className={styles.talkBtn} onClick={talkListProps.onClick}>
          <div className={styles.box1}>
            <div className={styles.box1_1}>
              <p className={styles.talkPartner}>{talkListProps.partner}</p>
            </div>
            <div className={styles.box1_2}>
              <p className={styles.date}>{talkListProps.date}</p>
            </div>
          </div>
          <div className={styles.box2}>
            <p className={styles.lastContent}>{talkListProps.lastContent}</p>
          </div>
        </button>
      </div>
    </>
  );

};


export default TalkList2;