import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './matiere.reducer';

export const MatiereDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const matiereEntity = useAppSelector(state => state.matiere.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="matiereDetailsHeading">
          <Translate contentKey="isimmManagerApp.matiere.detail.title">Matiere</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{matiereEntity.id}</dd>
          <dt>
            <span id="nomMatiere">
              <Translate contentKey="isimmManagerApp.matiere.nomMatiere">Nom Matiere</Translate>
            </span>
          </dt>
          <dd>{matiereEntity.nomMatiere}</dd>
          <dt>
            <Translate contentKey="isimmManagerApp.matiere.semestre">Semestre</Translate>
          </dt>
          <dd>{matiereEntity.semestre ? matiereEntity.semestre.id : ''}</dd>
          <dt>
            <Translate contentKey="isimmManagerApp.matiere.note">Note</Translate>
          </dt>
          <dd>{matiereEntity.note ? matiereEntity.note.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/matiere" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/matiere/${matiereEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MatiereDetail;
