import React from 'react';
import styles from '../../styles/talk/talkList.module.scss';
import { useSelector } from 'react-redux';
import { Rootstate } from '../../index';
import timeConvert from '../../utils/timeConvert';

interface MessageProps {
  contentInfo: ContentInfo[];
}

interface ContentInfo {
  id: number,
  senderNickname: string,
  receiverNickname: string,
  content: string,
  senderId: number,
  receiverId: number,
  messageRoomId: number
  createTime: string;
}


const Message2 = ({ contentInfo }: MessageProps) => {
  const store = useSelector((state: Rootstate) => state);
  console.log(contentInfo);

  if (!contentInfo) {
    return null;
  }


  return (
    <div>
      {contentInfo.map((content, idx) => (
        content.senderId === store.userInfoReducer.id ?
          <div className={styles.receive} key={idx}>
            <div className={styles.receiveTitle}>보낸 쪽지</div>
            <div className={styles.timeBox}>
              <p className={styles.receiveContent}>{content.content}</p>
              <p className={styles.timeX}>{timeConvert(content.createTime)}</p>
            </div>
          </div>
          :
          <div className={styles.send} key={idx}>
            <div className={styles.sendTitle}>받은 쪽지</div>
            <div className={styles.timeBox}>
              <p className={styles.sendContent}>{content.content}</p>
              <p className={styles.timeX}>{timeConvert(content.createTime)}</p>

            </div>
          </div>
      ))
      }
    </div>
  );
};
export default Message2;