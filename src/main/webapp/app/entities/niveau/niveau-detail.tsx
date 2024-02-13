import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './niveau.reducer';

export const NiveauDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const niveauEntity = useAppSelector(state => state.niveau.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="niveauDetailsHeading">
          <Translate contentKey="isimmManagerApp.niveau.detail.title">Niveau</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{niveauEntity.id}</dd>
          <dt>
            <span id="classe">
              <Translate contentKey="isimmManagerApp.niveau.classe">Classe</Translate>
            </span>
          </dt>
          <dd>{niveauEntity.classe}</dd>
          <dt>
            <span id="tp">
              <Translate contentKey="isimmManagerApp.niveau.tp">Tp</Translate>
            </span>
          </dt>
          <dd>{niveauEntity.tp}</dd>
          <dt>
            <span id="td">
              <Translate contentKey="isimmManagerApp.niveau.td">Td</Translate>
            </span>
          </dt>
          <dd>{niveauEntity.td}</dd>
          <dt>
            <Translate contentKey="isimmManagerApp.niveau.semestre">Semestre</Translate>
          </dt>
          <dd>{niveauEntity.semestre ? niveauEntity.semestre.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/niveau" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/niveau/${niveauEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NiveauDetail;
