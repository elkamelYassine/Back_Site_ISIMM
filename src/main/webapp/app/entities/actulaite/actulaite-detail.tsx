import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './actulaite.reducer';

export const ActulaiteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const actulaiteEntity = useAppSelector(state => state.actulaite.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="actulaiteDetailsHeading">
          <Translate contentKey="isimmManagerApp.actulaite.detail.title">Actulaite</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{actulaiteEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="isimmManagerApp.actulaite.date">Date</Translate>
            </span>
          </dt>
          <dd>{actulaiteEntity.date ? <TextFormat value={actulaiteEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="data">
              <Translate contentKey="isimmManagerApp.actulaite.data">Data</Translate>
            </span>
          </dt>
          <dd>{actulaiteEntity.data}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="isimmManagerApp.actulaite.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {actulaiteEntity.image ? (
              <div>
                {actulaiteEntity.imageContentType ? (
                  <a onClick={openFile(actulaiteEntity.imageContentType, actulaiteEntity.image)}>
                    <img src={`data:${actulaiteEntity.imageContentType};base64,${actulaiteEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {actulaiteEntity.imageContentType}, {byteSize(actulaiteEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/actulaite" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/actulaite/${actulaiteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActulaiteDetail;
