import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './seance.reducer';

export const SeanceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const seanceEntity = useAppSelector(state => state.seance.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="seanceDetailsHeading">
          <Translate contentKey="isimmManagerApp.seance.detail.title">Seance</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{seanceEntity.id}</dd>
          <dt>
            <span id="jour">
              <Translate contentKey="isimmManagerApp.seance.jour">Jour</Translate>
            </span>
          </dt>
          <dd>{seanceEntity.jour}</dd>
          <dt>
            <span id="numSeance">
              <Translate contentKey="isimmManagerApp.seance.numSeance">Num Seance</Translate>
            </span>
          </dt>
          <dd>{seanceEntity.numSeance}</dd>
          <dt>
            <span id="salle">
              <Translate contentKey="isimmManagerApp.seance.salle">Salle</Translate>
            </span>
          </dt>
          <dd>{seanceEntity.salle}</dd>
          <dt>
            <Translate contentKey="isimmManagerApp.seance.matiere">Matiere</Translate>
          </dt>
          <dd>{seanceEntity.matiere ? seanceEntity.matiere.id : ''}</dd>
          <dt>
            <Translate contentKey="isimmManagerApp.seance.niveau">Niveau</Translate>
          </dt>
          <dd>{seanceEntity.niveau ? seanceEntity.niveau.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/seance" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/seance/${seanceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SeanceDetail;
